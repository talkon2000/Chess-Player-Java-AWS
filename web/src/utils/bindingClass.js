export default class BindingClass {
    /**
     * Binds all of the methods to "this" object. These methods will now have the state of the instance object.
     * @param methods The name of each method to bind.
     * @param classInstance The instance of the class to bind the methods to.
     */
    bindClassMethods(methods, classInstance) {
        methods.forEach(method => {
            classInstance[method] = classInstance[method].bind(classInstance);
        });
    }
}