import ChessPlayerClient from '../api/chessPlayerClient';
import BindingClass from '../utils/bindingClass';

/**
 * The component that handles redirecting the user based on if they exist or not
 */
export default class Login extends BindingClass {

    constructor() {
        super();
        this.bindClassMethods(['getUser'], this);
        this.client = new ChessPlayerClient();
    }

    async getUser() {
        const errorMessageDisplay = document.getElementById("error-message");
        let user = await this.client.getPrivateUser((error) => {
            errorMessageDisplay.innerText = `Error: ${error.message}`;
            errorMessageDisplay.classList.remove('hidden');
        });
        if (!user) {
            user = await this.client.createUser((error) => {
                errorMessageDisplay.innerText = `Error: ${error.message}`;
                errorMessageDisplay.classList.remove('hidden');
            });
        }
        if (user) {
            window.location.href = '/user-home.html';
        }
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const login = new Login();
    login.getUser();
};

window.addEventListener('DOMContentLoaded', main);