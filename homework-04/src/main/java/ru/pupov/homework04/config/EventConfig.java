package ru.pupov.homework04.config;

import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.shell.boot.TerminalCustomizer;

//@Configuration
public class EventConfig {

//для асинхронной работы слушателей, имя бина важно
//	@Bean
	public ApplicationEventMulticaster applicationEventMulticaster() {
		SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
		eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
		return eventMulticaster;
	}

//для запуска из jar в cmd с поддержкой кирилицы
//	@Bean
//	@ConditionalOnProperty(name = "test-system.run-mode.use-console", havingValue = "true", matchIfMissing = false)
	public TerminalCustomizer terminalCustomizerCharset() {
		return builder -> builder.encoding(System.console().charset());
	}
}
