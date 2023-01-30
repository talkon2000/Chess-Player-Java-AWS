import BindingClass from "./bindingClass";

/**
 * Stores all of the data across the state. When the state changes in any way, execute all of the listeners registered
 * with the DataStore. This way pages can listen to changes for specific data and refresh the page accordingly.
 */
export default class DataStore extends BindingClass {

    constructor(initialState = {}) {
        super();
        this.bindClassMethods(['getState', 'get', 'setState', 'set', 'addChangeListener'], this);
        this.state = initialState;
        this.listeners = [];
    }

    /**
     * Get all of the data stored in the data store.
     */
    getState() {
        return this.state;
    }

    /**
     * Get a specific attribute out of the DataStore.
     * @param attribute The attribute to get.
     * @returns The current value of that attribute.
     */
    get(attribute) {
        return this.state[attribute];
    }

    /**
     * Merge the current state of the DataStore with the new state. If there are any overlapping keys, overwrite the
     * values with the new state. Then execute all of the registered listeners, so they can react to any potential data
     * updates.
     *
     * This is a fast way to update all data stored in the datastore.
     */
    setState(newState) {
        // ... is the spread operator. This allows us to pull out all of the keys and values of the existing state and
        // the new state and combine them into one new object.
        this.state = {...this.state, ...newState};
        this.listeners.forEach(listener => listener());
    }

    /**
     * Set or update the state of a specific attribute. Then execute all of the registered listeners, so they can react
     * to any potential data updates.
     * @param attribute The attribute to set or update.
     * @param value The value to give the attribute.
     */
    set(attribute, value) {
        this.state[attribute] = value;
        this.listeners.forEach(listener => listener());
    }

    /**
     * Add a listener. Whenever the state is changed in the DataStore all of the listeners will be executed.
     */
    addChangeListener(listener) {
        this.listeners.push(listener);
    }

}