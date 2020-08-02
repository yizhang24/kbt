package kiwibot;

import net.dv8tion.jda.api.OnlineStatus;

public class StatusHelper extends Main{

    private static OnlineStatus status;

    private static String statusMessage;

    @Override
    public void ChangeStatus(OnlineStatus _status){
        status = _status;
        System.out.println("Status Handler: Changing status to "+ _status.toString());
        super.ChangeStatus(_status);
    }
}
