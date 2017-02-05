package cs682.entities;

/**
 * Created by RyanZhu on 05/02/2017.
 */
public class BaseEntity {
    private boolean success;

    public BaseEntity(boolean success) {
        this.success = success;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
