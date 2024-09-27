package com.obi.dto;

import lombok.Data;

@Data
public class PromptBody {
    public String prompt;

    public String getPrompt() {
        return prompt;
    }
}
