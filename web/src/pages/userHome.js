import ChessPlayerClient from '../api/chessPlayerClient';
import Header from '../components/header';
import BindingClass from '../utils/bindingClass';
import DataStore from '../utils/DataStore';

/**
 * The component that handles the user's home page
 */
export default class UserHome extends BindingClass {

    constructor() {
        super();
        this.bindClassMethods(['mount', 'startGame'], this);
        this.client = new ChessPlayerClient();
    }

    mount() {
        var slider = document.getElementById("botDifficulty");
        var output = document.getElementById("output");
        output.innerHTML = slider.value; // Display the default slider value

        // Update the current slider value (each time you drag the slider handle)
        slider.oninput = function() {
          output.innerHTML = this.value;
        }
        document.getElementById("start").addEventListener('click', this.startGame);
    }

    async startGame() {
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

}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const userHome = new UserHome();
    userHome.mount();
};

window.addEventListener('DOMContentLoaded', main);