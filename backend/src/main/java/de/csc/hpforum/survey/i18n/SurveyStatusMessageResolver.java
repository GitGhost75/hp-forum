package de.csc.hpforum.survey.i18n;

import de.csc.hpforum.survey.model.entity.SurveyStatus;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class SurveyStatusMessageResolver {

  private final MessageSource messageSource;
  private final Map<String, String> cache = new ConcurrentHashMap<>();

  public SurveyStatusMessageResolver(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  public String resolve(SurveyStatus status, Locale locale) {
    if (status == null) {
      return null;
    }

    String cacheKey = status.name() + "_" + locale;
    return cache.computeIfAbsent(cacheKey, key -> {
      String message = messageSource.getMessage(
          status.getClass().getName() + "." + status.name(),
          null,
          status.name(),
          locale);
      return StringUtils.hasText(message) ? message : status.name();
    });
  }
}
