import ChessPlayerClient from '../api/chessPlayerClient';
import Header from '../components/header';
import BindingClass from '../utils/bindingClass';

/**
 * The component that handles the user's home page
 */
export default class UserHome extends BindingClass {

    constructor() {
        super();
        this.bindClassMethods(['mount', 'loadUserData', 'populateGameHistory', 'startGame', 'checkForEnterKey', 'search', 'resetAccount', 'toggleHidingMode', 'hideGames'], this);
        this.client = new ChessPlayerClient();
        this.header = new Header();
    }

    async mount() {
        this.header.addHeaderToPage();
        const navAccount = document.getElementById("navAccount");
        document.getElementById("navAccount").classList.add("active");

        const alerts = document.getElementById("errorAlerts");
        let user = await this.client.getPrivateUser();
        if (!user) {
            user = await this.client.createUser((error) => {
                const alert = this.client.createAlert("Your user could not be created.", "alert-danger");
                alerts.append(alert);
            });
        }

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
        document.getElementById("resetAccount").addEventListener('click', this.resetAccount);
        document.getElementById("toggleHide").addEventListener('click', this.toggleHidingMode);
        document.getElementById("submitHide").addEventListener('click', this.hideGames);

        this.loadUserData();
    }

    async loadUserData() {
        const alerts = document.getElementById("errorAlerts");
        const user = await this.client.getPrivateUser((error) => {
            alerts.append(this.client.createAlert(`<strong>Error:</strong> ${error.message}`, "alert-danger"));
        });
        if (user) {
            document.getElementById("user").classList.remove("hidden");
            document.getElementById("username").innerHTML = user.user.username;
            document.getElementById("email").innerHTML = user.user.email;
            document.getElementById("rating").innerHTML = "Rating: " + user.user.rating;
            this.populateGameHistory(user.user.username);
        }
        const spinner = document.getElementById("userSpinner");
        spinner.remove();
    }

    async populateGameHistory(username) {
        const alerts = document.getElementById("errorAlerts");

        const games = await this.client.getAllGames((error) => {
            alerts.append(this.client.createAlert(`<strong>Error:</strong> ${error.message}`, "alert-danger"));
        });
        if (games) {
            // set up monstrosities
            const notationToPieceMap = {"P": "♙", "R": "♖", "N": "♘", "B": "♗", "Q": "♕", "K": "♔", "p": "♟", "r": "♜", "n": "♞", "b": "♝", "q": "♛", "k": "♚"};
            const indexToPosition = ["a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8", "", "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7", "", "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6", "", "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5", "", "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4", "", "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3", "", "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2", "", "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"];
            const historyDiv = document.getElementById("pastGames");
            const currentDiv = document.getElementById("currentGames");
            games.forEach((game) => {

                const gameElement = document.createElement("td");
                gameElement.id = game.gameId;
                gameElement.notation = game.notation;
                gameElement.setAttribute("style", "position: relative;");
                const table = document.createElement("table");
                table.classList.add('chess-board');
                table.innerHTML = "<tbody><tr><td class='light' id='a8'></td><td class='dark' id='b8'></td><td class='light' id='c8'></td><td class='dark' id='d8'></td><td class='light' id='e8'></td><td class='dark' id='f8'></td><td class='light' id='g8'></td><td class='dark' id='h8'></td></tr><tr><td class='dark' id='a7'> </td><td class='light' id='b7'></td><td class='dark' id='c7'></td><td class='light' id='d7'></td><td class='dark' id='e7'></td><td class='light' id='f7'></td><td class='dark' id='g7'></td><td class='light' id='h7'></td></tr><tr><td class='light' id='a6'></td><td class='dark' id='b6'></td><td class='light' id='c6'></td><td class='dark' id='d6'></td><td class='light' id='e6'></td><td class='dark' id='f6'></td><td class='light' id='g6'></td><td class='dark' id='h6'></td></tr><tr><td class='dark' id='a5'></td><td class='light' id='b5'></td><td class='dark' id='c5'></td><td class='light' id='d5'></td><td class='dark' id='e5'></td><td class='light' id='f5'></td><td class='dark' id='g5'></td><td class='light' id='h5'></td></tr><tr><td class='light' id='a4'></td><td class='dark' id='b4'></td><td class='light' id='c4'></td><td class='dark' id='d4'></td><td class='light' id='e4'></td><td class='dark' id='f4'></td><td class='light' id='g4'></td><td class='dark' id='h4'></td></tr><tr><td class='dark' id='a3'></td><td class='light' id='b3'></td><td class='dark' id='c3'></td><td class='light' id='d3'></td><td class='dark' id='e3'></td><td class='light' id='f3'></td><td class='dark' id='g3'></td><td class='light' id='h3'></td></tr><tr><td class='light' id='a2'></td><td class='dark' id='b2'></td><td class='light' id='c2'></td><td class='dark' id='d2'></td><td class='light' id='e2'></td><td class='dark' id='f2'></td><td class='light' id='g2'></td><td class='dark' id='h2'></td></tr><tr><td class='dark' id='a1'> </td><td class='light' id='b1'></td><td class='dark' id='c1'></td><td class='light' id='d1'></td><td class='dark' id='e1'></td><td class='light' id='f1'></td><td class='dark' id='g1'></td><td class='light' id='h1'></td></tr></tbody>";
                const fen = game.notation;
                if (game.active == "true") {
                    currentDiv.append(gameElement);
                    gameElement.append(table);
                    table.addEventListener('click', function() {
                        window.location.href = "/game.html?gameId=" + table.parentElement.id;
                    });
                }
                else {
                    document.getElementById("toggleHide").classList.remove("hidden");
                    document.getElementById("submitHide").classList.remove("hidden");
                    const overlay = document.createElement("div");
                    overlay.classList.add("overlay");
                    overlay.classList.add("hidden");
                    overlay.classList.add("transparent");
                    overlay.innerHTML = "<p>✓</p>";
                    overlay.addEventListener("click", () => {
                        if (overlay.classList.contains("transparent")) {
                            overlay.classList.remove("transparent");
                        } else {
                            overlay.classList.add("transparent");
                        }
                    });
                    historyDiv.append(gameElement);
                    gameElement.append(overlay, table);

                    table.addEventListener('click', function() {
                        window.location.href = "/pastGame.html?gameId=" + table.parentElement.id;
                    });
                }

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
                        const square = document.getElementById(indexToPosition[i + b]);
                        square.innerHTML = notationToPieceMap[c];
                        square.removeAttribute('id');
                    }
                }
                gameElement.classList.add('game');
            });
        }
        document.getElementById("gameSpinner").remove();
    }

    async startGame() {
        const startButton = document.getElementById("start");
        startButton.disabled = true;
        const spinner = document.createElement("span");
        spinner.classList.add("spinner-border");
        spinner.classList.add("spinner-border-sm");
        spinner.setAttribute("role", "status");
        spinner.setAttribute("aria-hidden", "true");
        spinner.id = "startGameSpinner";
        startButton.prepend(spinner);
        const botDifficulty = document.getElementById("output").innerHTML;
        let authUserWhite = true;

        const alerts = document.getElementById("errorAlerts");
        const response = await this.client.createGame(authUserWhite, null, botDifficulty, (error) => {
            const alert = this.client.createAlert("The game could not be created.", "alert-warning", true);
            alerts.append(alert);
        });
        if (response) {
            window.location.href = "/game.html?gameId=" + response.gameId;
        }
        startButton.removeChild(spinner);
        startButton.disabled = false;
    }

    checkForEnterKey(event) {
        event.preventDefault();
        if (event.keyCode === 13) {
            this.search();
        }
    }

    async search() {
        const searchButton = document.getElementById("submitSearch");
        submitSearch.disabled = true;
        const spinner = document.createElement("span");
        spinner.classList.add("spinner-border");
        spinner.classList.add("spinner-border-sm");
        spinner.setAttribute("role", "status");
        spinner.setAttribute("aria-hidden", "true");
        spinner.id = "searchSpinner";
        searchButton.prepend(spinner);
        const alerts = document.getElementById("errorAlerts");
        const response = await this.client.getPublicUser(document.getElementById("searchInput").value, (error) => {
            alerts.append(this.client.createAlert("There is no user with that username.", "alert-info", true));
        });
        const searchResults = document.getElementById("searchResults");
        if (searchResults.children.length > 0) {
            searchResults.removeChild(searchResults.children[0]);
        }
        if (response) {
            const user = response.user;

            const userCard = document.createElement("div");
            userCard.classList.add("card");

            const username = document.createElement("div");
            username.classList.add("card-header");

            const rating = document.createElement("div");
            rating.classList.add("card-body");

            const gamesPlayed = document.createElement("div");
            gamesPlayed.classList.add("card-body");
            userCard.append(username, rating, gamesPlayed);
            username.innerText = user.username;
            rating.innerText = "Rating: " + user.rating;
            gamesPlayed.innerText = (user.games) ? user.games.length + " games played" : "0" + " games played";
            searchResults.append(userCard);
        }
        submitSearch.removeChild(spinner);
        submitSearch.disabled = false;
    }

    async resetAccount() {
        const confirm = window.confirm("Are you sure you want to reset your account?" +
                " You will still be able to use your login, but all of your data will be erased.");

        const resetButton = document.getElementById("resetAccount");
        resetButton.disabled = true;
        const spinner = document.createElement("span");
        spinner.classList.add("spinner-border");
        spinner.classList.add("spinner-border-sm");
        spinner.setAttribute("role", "status");
        spinner.setAttribute("aria-hidden", "true");
        spinner.id = "resetSpinner";
        resetButton.prepend(spinner);

        const alerts = document.getElementById("errorAlerts");
        if (confirm) {
            const response = await this.client.resetAccount((error) => {
                alerts.append(this.client.createAlert("There is no user with that username", "alert-warning", true));
            });
            if (response) {
                await this.client.logout();
                window.location.href = '/index.html';
            }
        }
        resetButton.removeChild(spinner);
        resetButton.disabled = false;
    }

    toggleHidingMode() {
        const hidingMode = document.getElementById("toggleHide").classList.contains("active");
        const submitHide = document.getElementById("submitHide");
        const historyDiv = document.getElementById("pastGames");
        const overlays = document.querySelectorAll("div.overlay");

        if (!hidingMode) {
            overlays.forEach(overlay => {
                overlay.classList.add("hidden");
            });
            submitHide.disabled = true;
        }

        if (hidingMode) {
            overlays.forEach(overlay => {
                overlay.classList.remove("hidden");
            });
            submitHide.disabled = false;
        }
    }

    async hideGames() {
        const hideButton = document.getElementById("submitHide");
        submitHide.disabled = true;
        const spinner = document.createElement("span");
        spinner.classList.add("spinner-border");
        spinner.classList.add("spinner-border-sm");
        spinner.setAttribute("role", "status");
        spinner.setAttribute("aria-hidden", "true");
        spinner.id = "hideSpinner";
        hideButton.prepend(spinner);

        const alerts = document.getElementById("errorAlerts");
        const overlays = document.querySelectorAll("div.overlay:not(.transparent)");
        const gameIds = [];
        if (overlays) {
            overlays.forEach(overlay => {
                gameIds.push(overlay.parentElement.id);
            });
        }
        if (gameIds.length > 0) {

            const confirm = window.confirm("Are you sure you want to hide these games? This will permanently remove them " +
                                            "from your account, but the rating gain/loss will still be reflected.");
            if (confirm) {
                const response = await this.client.hideGames(gameIds, error => {
                    alerts.append(this.client.createAlert(`<strong>Error:</strong> ${error.message}`, "alert-warning", true));
                });
                if (response.data.gameIds) {
                    response.data.gameIds.forEach(gameId => {
                        document.getElementById(gameId).remove();
                    });
                }
            }
        }
        const pastGames = document.getElementById("pastGames").querySelectorAll("table.chess-board");
        console.log(pastGames);
        if (pastGames.length == 0) {
            document.getElementById("toggleHide").classList.add("hidden");
            document.getElementById("submitHide").classList.add("hidden");
        }
        hideButton.removeChild(spinner);
        hideButton.disabled = false;
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const userHome = new UserHome();
    userHome.mount();
};

window.addEventListener('load', main);