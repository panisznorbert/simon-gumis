package panisz.norbert.simongumis.exceptions;

public class LetezoGumiException extends Exception {

    public LetezoGumiException(Integer id){
        super(id.toString());
    }
}
