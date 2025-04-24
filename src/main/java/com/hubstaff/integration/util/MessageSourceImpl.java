package com.hubstaff.integration.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MessageSourceImpl {

    private final MessageSource messageSource;

    public MessageSourceImpl(MessageSource messageSource)
    {
        this.messageSource=messageSource;
    }

    public String getMessage(String msgKey) {
        return messageSource.getMessage(msgKey, null, LocaleContextHolder.getLocale());
    }
}
