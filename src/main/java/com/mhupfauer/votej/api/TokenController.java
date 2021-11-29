package com.mhupfauer.votej.api;

import com.mhupfauer.votej.persistence.Entity.CastToken;
import com.mhupfauer.votej.persistence.Entity.VoterRegEnt;
import com.mhupfauer.votej.persistence.Entity.VoterTokenRecordEnt;
import com.mhupfauer.votej.persistence.Repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.List;

@RestController
@RequestMapping("api/event/{event_id}/ballot/{ballot_id/question/{question_id}/token")
public class TokenController {

    @Autowired
    private CastTokenRepository castTokenRepository;

    @Autowired
    private QuestionEntRepository questionEntRepository;

    @Autowired
    private VoterTokenRecordEntRepository voterTokenRecordEntRepository;

    @Autowired
    private UserEntRepository userEntRepository;

    @GetMapping("/")
    public Object getAllTokensOfQuestion(@PathVariable(name = "question_id") Long qid)
    {
        if(questionEntRepository.findById(qid).isEmpty())
            return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
        return voterTokenRecordEntRepository.getVoterTokenRecordEntsByQuestion(questionEntRepository.findById(qid).get());
    }

    @GetMapping("/user/{user_id}")
    public Object getTokenStatusForUser(@PathVariable(name = "user_id") Long uid, @PathVariable(name = "question_id") Long qid)
    {
        if(userEntRepository.findById(uid).isEmpty() || questionEntRepository.findById(qid).isEmpty())
            return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
        return voterTokenRecordEntRepository.getVoterTokenRecordEntsByUserAndQuestion(
                userEntRepository.findById(uid).get(),
                questionEntRepository.findById(qid).get());
    }

    @GetMapping("/user/{user_id}/issue")
    public Object issueTokenForUser(@PathVariable(name = "user_id") Long uid,
                                    @PathVariable(name = "question_id") Long qid) {
        if(userEntRepository.findById(uid).isEmpty() || questionEntRepository.findById(qid).isEmpty())
            return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);

        final boolean[] is_not_allowed = {true};
        questionEntRepository.findById(qid).get().getBallot().getVoters().forEach((voter) -> {
            if (voter.getUser() == userEntRepository.findById(uid).get())
                is_not_allowed[0] = false;
        });
        if(is_not_allowed[0])
            return new ResponseEntity<HttpStatus>(HttpStatus.FORBIDDEN);

        if(voterTokenRecordEntRepository.getVoterTokenRecordEntsByUserAndQuestion(
                userEntRepository.findById(uid).get(),
                questionEntRepository.findById(qid).get()
        ).size() == 0) {
            VoterTokenRecordEnt voterTokenRecordEnt = VoterTokenRecordEnt.builder()
                    .user(userEntRepository.findById(uid).get())
                    .question(questionEntRepository.findById(qid).get())
                    .build();

            CastToken castToken = CastToken.builder()
                    .token(new SecureRandom().nextLong())
                    .question(questionEntRepository.findById(qid).get())
                    .build();

            voterTokenRecordEntRepository.save(voterTokenRecordEnt);
            castTokenRepository.save(castToken);

            voterTokenRecordEnt = null;
            return castToken;
        } else {
            return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/")
    public ResponseEntity<HttpStatus> deleteAllTokensForQuestion(@PathVariable(name = "question_id") Long qid)
    {
        if(questionEntRepository.findById(qid).isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        castTokenRepository.getCastTokensByQuestion(questionEntRepository.findById(qid).get()).forEach((question) -> {
            castTokenRepository.delete(question);
        });
        voterTokenRecordEntRepository.getVoterTokenRecordEntsByQuestion(questionEntRepository.findById(qid).get()).forEach((voterToken) -> {
            voterTokenRecordEntRepository.delete(voterToken);
        });

        return new ResponseEntity<>(HttpStatus.OK);
    }
}