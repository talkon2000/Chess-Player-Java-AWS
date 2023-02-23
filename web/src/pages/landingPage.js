import Header from '../components/header';
import BindingClass from '../utils/bindingClass';

/**
 * The component that handles the landing page logic
 */
export default class LandingPage extends BindingClass {

    constructor() {
            super();
            this.bindClassMethods(['mount'], this);
            this.header = new Header();
    }

     /**
      * Add the header to the page and load the ChessPlayerClient.
      */
    async mount() {
        this.header.addHeaderToPage();
        document.getElementById("navHome").classList.add("active");
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