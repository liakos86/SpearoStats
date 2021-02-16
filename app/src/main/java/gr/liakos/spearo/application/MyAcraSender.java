package gr.liakos.spearo.application;

import gr.liakos.spearo.SpearoApplication;
import gr.liakos.spearo.mongo.SyncHelper;

import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

/**
 * Created by liakos on 21/9/2015.
 */

public class MyAcraSender implements ReportSender {

    SpearoApplication application;

    public MyAcraSender(SpearoApplication application) {
        this.application = application;
    }

    @Override
    public void send(CrashReportData report) throws ReportSenderException {

    	new SyncHelper(application).sendCrashReport(report);
    	
    }
}
