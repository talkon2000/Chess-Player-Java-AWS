import ChessPlayerClient from '../api/chessPlayerClient';
import BindingClass from '../utils/bindingClass';

/**
 * The component that handles redirecting the user based on if they exist or not
 */
export default class Login extends BindingClass {

    constructor() {
        super();
        this.bindClassMethods(['login'], this);
        this.client = new ChessPlayerClient();
    }

    login() {
        
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const login = new Login();
    login.mount();
};

window.addEventListener('DOMContentLoaded', main);