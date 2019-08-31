package delivery;

class Status {

    private String result;
    private String message;

    public String getResult() { return result; }
    public String getMessage() { return message; }

    public Status(String theMessage) {
        this.result = theMessage.length()==0 ? "OK" : "Failed";
        this.message = theMessage;
    }
}
