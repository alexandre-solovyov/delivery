package delivery;

class Messages {
	
	public static final String HELLO = "Delivery service is available";
	
	public static final String ONLY_PRODUCER_ORDER = "Only producer/admin can change state of order";
	public static final String ONLY_PRODUCER_PRODUCT = "Only producer/admin can change products";
	public static final String ONLY_ADMIN_ROLE = "Only admin can change user's role";
	
	public static final String CANNOT_FIND_ORDER = "Cannot find order: ";
	public static final String CANNOT_FIND_PRODUCT = "Cannot find product with code: ";
	public static final String CANNOT_FIND_USER = "Cannot find user with login: ";
	
	public static final String NON_EMPTY_NAME = "The name must not be empty";
	public static final String POS_CODE = "The code must be positive";
	public static final String POS_PRICE = "The price must be positive";
	
    public static final String AUTH_REQ = "Basic authorization is required";
    public static final String USER_EXISTS = "A user with such login already exists";
    public static final String PRODUCT_EXISTS = "A product with such code already exists";
    public static final String INVALID_LOG_PWD = "Invalid login/password";
}
