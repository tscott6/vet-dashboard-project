

import data.AnimalDB;
import data.UserDB;
import data.AdminDB;
import domain.animal.AnimalRepository;
import domain.animal.AnimalService;
import domain.user.AdminRepository;
import domain.user.AdminService;
import domain.user.UserRepository;
import domain.user.UserService;
import errors.GlobalExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

class Configuration {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final AnimalRepository ANIMAL_REPOSITORY = new AnimalDB();
    private static final AnimalService ANIMAL_SERVICE = new AnimalService(ANIMAL_REPOSITORY);
    private static final UserRepository USER_REPOSITORY = new UserDB();
    private static final UserService USER_SERVICE = new UserService(USER_REPOSITORY);
    private static final AdminRepository ADMIN_REPOSITORY = new AdminDB();
    private static final AdminService ADMIN_SERVICE = new AdminService(ADMIN_REPOSITORY);
    private static final GlobalExceptionHandler GLOBAL_ERROR_HANDLER = new GlobalExceptionHandler(OBJECT_MAPPER);

    static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    static AnimalService getAnimalService() {
        return ANIMAL_SERVICE;
    }

    static AnimalRepository getAnimalRepository() {
        return ANIMAL_REPOSITORY;
    }
    
    static UserService getUserService() {
    	return USER_SERVICE;
    }
    
    static AdminService getAdminService() {
    	return ADMIN_SERVICE;
    }

    public static GlobalExceptionHandler getErrorHandler() {
        return GLOBAL_ERROR_HANDLER;
    }
}
