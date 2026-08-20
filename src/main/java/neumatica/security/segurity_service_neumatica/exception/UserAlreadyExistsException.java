package neumatica.security.segurity_service_neumatica.exception;

public class UserAlreadyExistsException extends RuntimeException{

	public UserAlreadyExistsException(String message) {
        super(message);
    }
}
