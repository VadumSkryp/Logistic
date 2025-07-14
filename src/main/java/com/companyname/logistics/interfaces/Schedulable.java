package com.companyname.logistics.interfaces;

import java.time.LocalDateTime;

public interface Schedulable {

    void schedule(LocalDateTime startTime);

    LocalDateTime getScheduledStartTime();

}
