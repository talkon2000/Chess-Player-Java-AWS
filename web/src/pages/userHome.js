import ChessPlayerClient from '../api/chessPlayerClient';
import Header from '../components/header';
import BindingClass from '../utils/bindingClass';

/**
 * The component that handles the user's home page
 */
export default class UserHome extends BindingClass {

    constructor() {
        super();
        this.bindClassMethods(['mount', 'loadUserData', 'populateGameHistory', 'startGame', 'checkForEnterKey', 'search'], this);
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
            {notation: "r1b1k1nr/p2p1pNp/n2B4/1p1NP2P/6P1/3P1Q2/P1P1K3/q5b1", gameId: "basil3"},
            {notation: "r1b1k1nr/p2p1pNp/n2B4/1p1NP2P/6P1/3P1Q2/P1P1K3/q5b1", gameId: "basil4"}];
        if (games) {
            // set up monstrosities
            const notationToPieceMap = {"P": "♙", "R": "♖", "N": "♘", "B": "♗", "Q": "♕", "K": "♔", "p": "♟", "r": "♜", "n": "♞", "b": "♝", "q": "♛", "k": "♚"};
            const indexToPosition = ["a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8", "", "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7", "", "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6", "", "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5", "", "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4", "", "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3", "", "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2", "", "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"];
            const gamesDiv = document.getElementById("games");
            games.forEach((game) => {

                const gameElement = document.createElement("div");
                gameElement.id = game.gameId;
                gameElement.notation = game.notation;
                gameElement.innerHTML = "<table class='chess-board'><tbody><tr><th></th><th>a</th><th>b</th><th>c</th><th>d</th><th>e</th><th>f</th><th>g</th><th>h</th></tr><tr><th>8</th><td class='light' id='a8'></td><td class='dark' id='b8'></td><td class='light' id='c8'></td><td class='dark' id='d8'></td><td class='light' id='e8'></td><td class='dark' id='f8'></td><td class='light' id='g8'></td><td class='dark' id='h8'></td></tr><tr><th>7</th><td class='dark' id='a7'> </td><td class='light' id='b7'></td><td class='dark' id='c7'></td><td class='light' id='d7'></td><td class='dark' id='e7'></td><td class='light' id='f7'></td><td class='dark' id='g7'></td><td class='light' id='h7'></td></tr><tr><th>6</th><td class='light' id='a6'></td><td class='dark' id='b6'></td><td class='light' id='c6'></td><td class='dark' id='d6'></td><td class='light' id='e6'></td><td class='dark' id='f6'></td><td class='light' id='g6'></td><td class='dark' id='h6'></td></tr><tr><th>5</th><td class='dark' id='a5'></td><td class='light' id='b5'></td><td class='dark' id='c5'></td><td class='light' id='d5'></td><td class='dark' id='e5'></td><td class='light' id='f5'></td><td class='dark' id='g5'></td><td class='light' id='h5'></td></tr><tr><th>4</th><td class='light' id='a4'></td><td class='dark' id='b4'></td><td class='light' id='c4'></td><td class='dark' id='d4'></td><td class='light' id='e4'></td><td class='dark' id='f4'></td><td class='light' id='g4'></td><td class='dark' id='h4'></td></tr><tr><th>3</th><td class='dark' id='a3'></td><td class='light' id='b3'></td><td class='dark' id='c3'></td><td class='light' id='d3'></td><td class='dark' id='e3'></td><td class='light' id='f3'></td><td class='dark' id='g3'></td><td class='light' id='h3'></td></tr><tr><th>2</th><td class='light' id='a2'></td><td class='dark' id='b2'></td><td class='light' id='c2'></td><td class='dark' id='d2'></td><td class='light' id='e2'></td><td class='dark' id='f2'></td><td class='light' id='g2'></td><td class='dark' id='h2'></td></tr><tr><th>1</th><td class='dark' id='a1'> </td><td class='light' id='b1'></td><td class='dark' id='c1'></td><td class='light' id='d1'></td><td class='dark' id='e1'></td><td class='light' id='f1'></td><td class='dark' id='g1'></td><td class='light' id='h1'></td></tr></tbody></table>";
                const fen = game.notation;
                gamesDiv.append(gameElement);

                function isNumeric(str) {
                    return /^\d+$/.test(str);
                }
                // put the pieces where they go
                for (let i = 0, b = 0; i + b < 71; i++) {
                    const c = fen[i];
                    if (isNumeric(c)) {
                        const num = parseInt(fen[i]) - 1;
                        for (let index = 0; index < num + 1; index++) {
                            const square = document.getElementById(indexToPosition[i + b + index]);
                            square.removeAttribute('id');
                        }
                        b += num
                    }
                    else if (c == "/") {
                        continue;
                    }
                    else {
                        let newPiece = document.createElement("p");
                        const square = document.getElementById(indexToPosition[i + b]);
                        square.append(newPiece);
                        square.removeAttribute('id');
                        newPiece.innerHTML = notationToPieceMap[c];
                    }
                }
                gameElement.classList.add('game');
                console.log(gameElement);

            });
        }0
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