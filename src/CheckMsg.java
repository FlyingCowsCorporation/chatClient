public class CheckMsg implements Runnable  {

    private Program program;
    public CheckMsg(Program program) {
        this.program = program;
    }

    public void run() {
        while(true){
            program.Receive();}
    }
}