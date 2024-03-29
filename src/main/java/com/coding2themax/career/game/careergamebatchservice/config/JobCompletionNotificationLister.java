package com.coding2themax.career.game.careergamebatchservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationLister implements JobExecutionListener {

  private static final Logger LOG = LoggerFactory.getLogger(JobCompletionNotificationLister.class);

  private final JdbcTemplate jdbcTemplate;

  public JobCompletionNotificationLister(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
      LOG.info("Job finished");

    }
  }

}
