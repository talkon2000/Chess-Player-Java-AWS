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
        this.header.addHeaderToPage();
        const currentUser = await this.client.getIdentity();
        if (currentUser) {
            window.location.href = "user-home.html";
        }
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