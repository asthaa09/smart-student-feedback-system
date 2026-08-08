package com.smartfeedback.service;

import com.smartfeedback.enums.SentimentType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SentimentService {
    private static final List<String> POSITIVE_WORDS = List.of("good", "great", "excellent", "helpful", "amazing", "clear", "supportive");
    private static final List<String> NEGATIVE_WORDS = List.of("bad", "poor", "boring", "confusing", "worst", "rude", "slow");

    public SentimentType classify(String comment) {
        String normalized = comment.toLowerCase();
        long positiveHits = POSITIVE_WORDS.stream().filter(normalized::contains).count();
        long negativeHits = NEGATIVE_WORDS.stream().filter(normalized::contains).count();

        if (positiveHits > negativeHits) {
            return SentimentType.POSITIVE;
        }
        if (negativeHits > positiveHits) {
            return SentimentType.NEGATIVE;
        }
        return SentimentType.NEUTRAL;
    }
}
