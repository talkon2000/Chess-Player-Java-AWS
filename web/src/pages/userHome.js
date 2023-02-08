import ChessPlayerClient from '../api/chessPlayerClient';
import Header from '../components/header';
import BindingClass from '../utils/bindingClass';

/**
 * The component that handles the user's home page
 */
export default class UserHome extends BindingClass {

    constructor() {
        super();
        this.bindClassMethods(['mount', 'loadUserData', 'populateGameHistory', 'startGame','checkForEnterKey', 'search'], this);
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

        this.loadUserData();
    }

    async loadUserData() {
        const errorMessageDisplay = document.getElementById('error-message');
        errorMessageDisplay.innerText = '';
        errorMessageDisplay.classList.add('hidden');

        const user = await this.client.getPrivateUser((error) => {
            errorMessageDisplay.innerText = `Error: ${error.message}`;
            errorMessageDisplay.classList.remove('hidden');
        });
        if (user) {
            document.getElementById("username").innerHTML = user.user.username;
            document.getElementById("email").innerHTML = user.user.email;
            document.getElementById("rating").innerHTML = user.user.rating;
            this.populateGameHistory(user.user.username);
        }
    }

    async populateGameHistory(username) {
        //const games = await this.client.getAllGames(username);
        games = [{notation: "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", gameId: "basil"},
            {notation: "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2", gameId: "basil2"},
            {notation: "r1b1k1nr/p2p1pNp/n2B4/1p1NP2P/6P1/3P1Q2/P1P1K3/q5b1", gameId: "basil3"}];
        if (games) {
            const gamesDiv = document.getElementById("games");
            games.forEach((game) => {

                const gameElement = document.createElement("iframe");
                gameElement.src = "/chessBoard.html";
                gameElement.notation = game.notation;
                console.log(gameElement);

                gameElement.addEventListener('load', onLoad);
                function onLoad(event) {
                    gameElement.contentWindow.postMessage(game.notation, "*");
                }
                gameElement.id = game.gameId;
                gameElement.width = 600;
                gameElement.height = 600;
                gameElement.frameBorder = 0;
                gamesDiv.append(gameElement);
            });
        }
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