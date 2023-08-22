package ru.pupov.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import ru.pupov.config.LocaleProvider;
import ru.pupov.service.LocalizationService;

@RequiredArgsConstructor
public class LocalizationServiceImpl implements LocalizationService {

    private final LocaleProvider localeProvider;

    private final MessageSource messageSource;

    @Override
    public String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, localeProvider.getCurrent());
    }
}
