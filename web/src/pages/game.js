import ChessPlayerClient from '../api/chessPlayerClient';
import Header from '../components/header';
import BindingClass from '../utils/bindingClass';
import DataStore from '../utils/DataStore';

/**
 * The game component for the website.
 */
export default class Game extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount', 'clientLoaded', 'submitMove'], this);
        this.dataStore = new DataStore();
        this.header = new Header();
    }

     /**
      * Add the header to the page and load the ChessPlayerClient.
      */
    mount() {
        document.getElementById('submit').addEventListener('click', this.submitMove);

        this.header.addHeaderToPage();
        this.client = new ChessPlayerClient();
        const d2 = document.getElementById('d2');
        d2.innerText = 'hello';
        this.clientLoaded;
    }

    async clientLoaded() {
        const urlParams = new URLSearchParams(window.location.search);
        const gameId = urlParams.get('id');
        this.dataStore.set('gameId', gameId);
    }

    /**
     * Submit the move to the database and draw the new move.
     */
     async submitMove() {
         const errorMessageDisplay = document.getElementById('error-message');
         errorMessageDisplay.innerText = '';
         errorMessageDisplay.classList.add('hidden');

        const gameId = dataStore.get(gameId);
        const move = dataStore.get(move);

        const submitButton = document.getElementById('submit');
        const origButtonText = submitButton.innerText;
        submitButton.innerText = 'Loading...';

        const response = await this.client.getNextMove(gameId, move, (error) => {
            submitButton.innerText = origButtonText;
            errorMessageDisplay.innerText = `Error: ${error.message}`;
            errorMessageDisplay.classList.remove('hidden');
        })
     }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const game = new Game();
    game.mount();
};

window.addEventListener('DOMContentLoaded', main);