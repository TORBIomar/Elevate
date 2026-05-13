package ma.emsi.elevate.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecruiterStatsResponse {

private long totalJobsOffered;

private long totalApplicationsReceived;

private long totalPendingApplications;

private long totalInterviewsScheduled;
}

