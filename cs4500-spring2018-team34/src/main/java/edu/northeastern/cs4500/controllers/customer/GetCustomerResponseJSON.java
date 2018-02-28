package java.edu.northeastern.cs4500.controllers.customer;
import java.util.List;

public class GetCustomerResponseJSON {
    private List<Customer> result;
    private String message;

    public List<Customer> getResult() {
        return result;
    }

    public void setResult(List<Customer> result) {
        this.result = result;
    }

    public GetCustomerResponseJSON withResult(List<Customer> result) {
        this.setResult(result);
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public GetCustomerResponseJSON withMessage(String message) {
        this.setMessage(message);
        return this;
    }
}
