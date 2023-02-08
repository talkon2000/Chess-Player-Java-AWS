import ChessPlayerClient from '../api/chessPlayerClient';
import Header from '../components/header';
import BindingClass from '../utils/bindingClass';

/**
 * The component that handles the user's home page
 */
export default class UserHome extends BindingClass {

    constructor() {
        super();
        this.bindClassMethods(['mount', 'startGame','checkForEnterKey', 'search'], this);
        this.client = new ChessPlayerClient();
        this.header = new Header();
    }

    mount() {
        this.header.addHeaderToPage();
        var slider = document.getElementById("botDifficulty");
        var output = document.getElementById("output");
        output.innerHTML = slider.value; // Display the default slider value

        // Update the current slider value (each time you drag the slider handle)
        slider.oninput = function() {
          output.innerHTML = this.value;
        }
        document.getElementById("start").addEventListener('click', this.startGame);
        document.getElementById("submitSearch").addEventListener('click', this.search);
        document.getElementById("searchInput").addEventListener('keyup', this.checkForEnterKey);
    }

    async startGame() {
        const errorMessageDisplay = document.getElementById('error-message');
        errorMessageDisplay.innerText = '';
        errorMessageDisplay.classList.add('hidden');
        const botDifficulty = document.getElementById("output").innerHTML;
        let authUserWhite = document.getElementById("authUserWhite").value;
        if (!authUserWhite) {
            authUserWhite = false;
        }
        const response = await this.client.createGame(authUserWhite, null, botDifficulty, (error) => {
            errorMessageDisplay.innerText = `Error: ${error.message}`;
            errorMessageDisplay.classList.remove('hidden');
        });
        console.log(response);
        if (response) {
            window.location.href = "/game.html?gameId=" + response.gameId;
        }
    }

    checkForEnterKey(event) {
        event.preventDefault();
        if (event.keyCode === 13) {
            this.search();
        }
    }

    async search() {
        const errorMessageDisplay = document.getElementById('error-message');
        errorMessageDisplay.innerText = '';
        errorMessageDisplay.classList.add('hidden');
        const response = await this.client.searchUsers(document.getElementById("searchInput").value, (error) => {
            errorMessageDisplay.innerText = `Error: ${error.message}`;
            errorMessageDisplay.classList.remove('hidden');
        });

        if (response) {
            window.location.href = "/user.html?username=" + response.username;
        }
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const userHome = new UserHome();
    userHome.mount();
};

window.addEventListener('DOMContentLoaded', main);