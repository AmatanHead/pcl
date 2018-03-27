package com.github.amatanhead.pcl.parser;


/**
 * Class for representing results of parsing.
 */
public class RDPResult {
    private boolean success;
    private String errorMessage;
    private Object result;

    /**
     * Construct a default result.
     *
     * @param success flag that indicates success.
     * @param result  a result of execution, either a token, a result of applying a functor to a token, or maybe
     *                something completely different.
     */
    protected RDPResult(boolean success, Object result) {
        this.success = success;
        this.result = result;
        this.errorMessage = null;
    }

    /**
     * Shortcut for constructing an error result.
     *
     * @param errorMessage error message or `null`.
     */
    protected RDPResult(String errorMessage) {
        this.success = false;
        this.errorMessage = errorMessage;
        this.result = null;
    }

    /**
     * Indicates that parsing was successful.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Overwrite success flag.
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Error message if parsing wasn't successful. Can be `null` (even when {@link #isSuccess()} is false).
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Overwrite error message.
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Result of execution. Expect this to be `null` if {@link #isSuccess()} is false.
     */
    public Object getResult() {
        return result;
    }

    /**
     * Overwrite the result.
     */
    public void setResult(Object result) {
        this.result = result;
    }
}
