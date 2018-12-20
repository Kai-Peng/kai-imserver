package im.kai.server.service.message.validator;

public abstract class AbstractValidator {
    protected AbstractValidator nextAbstract;

    public void setNextAbstract(AbstractValidator nextValidator){
        this.nextAbstract = nextValidator;
    }

    public boolean validateConditions(){
        Boolean validateRslt = validate();
        if(!validateRslt){
            return validateRslt;
        }else {
            return nextAbstract.validateConditions();
        }
    }

    abstract protected boolean validate();
}
