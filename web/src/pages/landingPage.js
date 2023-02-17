import ChessPlayerClient from '../api/chessPlayerClient';
import Header from '../components/header';
import BindingClass from '../utils/bindingClass';
import DataStore from '../utils/DataStore';

/**
 * The component that handles the landing page logic
 */
export default class LandingPage extends BindingClass {

    constructor() {
            super();
            this.bindClassMethods(['mount'], this);
            this.header = new Header();
            this.client = new ChessPlayerClient();
    }

     /**
      * Add the header to the page and load the ChessPlayerClient.
      */
    async mount() {
        const navItem = document.createElement('li');
        navItem.classList.add("nav-item");
        const currentUser = await this.client.getIdentity();
        let childContent;
        if (currentUser) {
            childContent = this.createLogoutButton(currentUser)
            document.getElementById("myAccount").classList.remove("hidden");
        } else {
            childContent = this.createLoginButton();
        }

        document.getElementById("currentUser").appendChild(navItem);
        navItem.appendChild(childContent);
    }

    createLoginButton() {
        return this.createButton('Login', this.client.login);
    }

    createLogoutButton(currentUser) {
        return this.createButton(`Logout: ${currentUser.username}`, this.client.logout);
    }

    createButton(text, clickHandler) {
        const button = document.createElement('a');
        button.classList.add("btn");
        button.classList.add("btn-success");
        button.href = '#';
        button.innerText = text;

        button.addEventListener('click', async () => {
            await clickHandler();
        });

        return button;
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const landing = new LandingPage();
    landing.mount();
};

window.addEventListener('DOMContentLoaded', main);