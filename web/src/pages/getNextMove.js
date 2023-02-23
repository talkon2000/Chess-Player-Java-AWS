import ChessPlayerClient from '../api/chessPlayerClient';
import Header from '../components/header';
import BindingClass from '../utils/bindingClass';
import DataStore from '../utils/DataStore';

/**
 * The component that handles the logic to play chess for the website.
 */
export default class GetNextMove extends BindingClass {

    constructor() {
        super();
        this.bindClassMethods(['mount', 'setUpBoard', 'reloadMoves', 'drag', 'doPromotion', 'doCastle', 'doCastleUser', 'submitMove', 'cancel', 'resign'], this);
        this.dataStore = new DataStore();
        this.header = new Header();
    }

     /**
      * Add the header to the page and load the ChessPlayerClient.
      */
    mount() {
        this.header.addHeaderToPage();
        this.client = new ChessPlayerClient();

        let url = new URL(window.location.href);
        let gameId = url.searchParams.get("gameId");
        this.dataStore.set("gameId", gameId);

        this.setUpBoard();

        document.getElementById('submit').addEventListener('click', this.submitMove);
        document.getElementById('submit').disabled = true;
        document.getElementById('cancel').addEventListener('click', this.cancel);
        document.getElementById('cancel').disabled = true;
        document.getElementById('resign').addEventListener('click', this.resign);
    }

    async setUpBoard() {
        const alerts = document.getElementById('errorAlerts');
        const alert = this.client.createAlert("Preparing game", "alert-info");
        alerts.append(alert);
        // Get game
        const response = await this.client.getGame(this.dataStore.get("gameId"), (error) => {
            alerts.append(this.client.createAlert(`<strong>Error:</strong> ${error.message}`, "alert-danger"));
        });
        alerts.removeChild(alert);
        const game = response.game;
        if (game.winner) {
            document.getElementById('resign').disabled = true;
            document.getElementById('errorAlerts').append(
                    this.client.createAlert("This game is already over. You can find it in your games history if " +
                    "you would like to replay it.", "alert-info"));
        }
        const fen = game.notation;
        const validMoves = game.validMoves.split(",");

        // set up monstrosities
        const notationToPieceMap = {"P": "♙", "R": "♖", "N": "♘", "B": "♗", "Q": "♕", "K": "♔", "p": "♟", "r": "♜", "n": "♞", "b": "♝", "q": "♛", "k": "♚"};
        const indexToPosition = ["a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8", "", "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7", "", "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6", "", "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5", "", "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4", "", "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3", "", "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2", "", "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"];

        function isNumeric(str) {
            return /^\d+$/.test(str);
        }
        // put the pieces where they go
        for (let i = 0, b = 0; i + b < 71; i++) {
            const c = fen[i];
            if (isNumeric(c)) {
                b += parseInt(fen[i]) - 1;
            }
            else if (c == "/") {
                continue;
            }
            else {
                let newPiece = document.createElement("chess-piece");
                document.getElementById(indexToPosition[i + b]).append(newPiece);
                newPiece.innerHTML = notationToPieceMap[c];
                newPiece.validMoves = [];
                if (c >= 'a' && c <= 'z') {
                    newPiece.classList.add("black");
                }
                else {
                    newPiece.classList.add("white");
                    newPiece.addEventListener('mousedown', this.drag);
                }
            }
        }

        // set up valid moves
        if (!game.winner) {
            validMoves.filter(move => move != "").forEach((move) => {
                let validFrom = move.slice(0, 2);
                let validTo = move.slice(2, 4);
                const fromPiece = document.getElementById(validFrom).firstElementChild;
                fromPiece.validMoves.push(validTo);
            });
        }
    }

    drag(event) {
        if (!document.getElementById('submit').disabled) {
            return;
        }
        const piece = event.target;
        if (!piece.validMoves) {
            return;
        }
        const origParent = piece.parentElement;

        piece.style.position = 'absolute';
        piece.style.zIndex = 1000;

        // move it out of any current parents directly into body
        // to make it positioned relative to the body
        document.body.append(piece);

        // move our absolutely positioned piece under the pointer
        // element that we're flying over right now
        let elemBelow = null;
        function onMouseMove(event) {
            piece.style.left = event.pageX - piece.offsetWidth / 2 + 'px';
            piece.style.top = event.pageY - piece.offsetHeight / 2 + 'px';
            piece.hidden = true;
            elemBelow = document.elementFromPoint(event.clientX, event.clientY);
            piece.hidden = false;
        }

        // Initially move piece under cursor and initialize elemBelow
        onMouseMove(event);

        // (2) move the piece on mousemove
        document.addEventListener('mousemove', onMouseMove);

        let origX = event.pageX;
        let origY = event.pageY;
        // (3) drop the piece, remove unneeded handlers
        piece.onmouseup = () => {
            var captured = null;
            if (elemBelow.nodeName == "BODY") {
                origParent.append(piece);
            }
            // if you drop the piece on another piece, keep track of that piece in captured
            if (elemBelow.children) {
                captured = elemBelow.children[0];
            }
            if (elemBelow.nodeName == "CHESS-PIECE") {
                captured = elemBelow;
                elemBelow = elemBelow.parentElement;
            }
            if (piece.validMoves.includes(elemBelow.id)) {
                let promoted = "";
                if (captured) {
                    elemBelow.removeChild(captured);
                }
                elemBelow.append(piece);
                if ((elemBelow.id.includes("1") || elemBelow.id.includes("8")) && (piece.innerHTML == "♟" || piece.innerHTML == "♙")) {
                    promoted = true;
                    promoted = this.doPromotion(piece);
                }
                else {
                document.getElementById('submit').disabled = false;
                document.getElementById('cancel').disabled = false;
                }
                this.dataStore.set("move", {"to": elemBelow, "from": origParent, "captured": captured});
                this.doCastleUser();
            }
            else {
                origParent.append(piece);
            }
            piece.style.position = 'static';
            document.removeEventListener('mousemove', onMouseMove);
            piece.onmouseup = null;
        };
    }

    // Logic in case a promotion happens
    doPromotion(piece)  {
        let promotion = document.getElementById("promotion");
        let promotedTo = "";
        promotion.classList.remove('hidden');

        promotion.addEventListener("click", (event) => {
            let elemBelow = document.elementFromPoint(event.clientX, event.clientY);
            if (elemBelow.nodeName == "CHESS-PIECE") {
                elemBelow = elemBelow.parentElement;
            }
            piece.innerHTML = elemBelow.innerHTML;
            promotedTo = elemBelow.id;
            promotion.classList.add('hidden');
            document.getElementById('submit').disabled = false;
            document.getElementById('cancel').disabled = false;
            const move = this.dataStore.get("move");
            move.promoted = promotedTo;
            this.dataStore.set("move", move);
        }, { once: true });
    }

    /**
     * Overloaded method to handle the engine move or the user move
     */
    doCastle(move, piece) {
        const from = move.slice(0, 1);
        const to = move.slice(2, 3);
        // Check if the piece is a white king
        if (piece.innerHTML == "♔") {
            // Castling king side. move the rook to the f file
            if (from == "e" && to == "g") {
                const rookSquare = document.getElementById("h1");
                const rook = rookSquare.removeChild(rookSquare.children[0]);
                document.getElementById("f1").append(rook);
                return "K";
            }
            // Castling queen side. move the rook to the d file
            if (from == "e" && to == "c") {
                const rookSquare = document.getElementById("a1");
                const rook = rookSquare.removeChild(rookSquare.children[0]);
                document.getElementById("d1").append(rook);
                return "Q";
            }
        }
        // Check if the piece is a black king
        else if (piece.innerHTML == "♚") {
            // Castling king side. move the rook to the f file
            if (from == "e" && to == "g") {
                const rookSquare = document.getElementById("h8");
                const rook = rookSquare.removeChild(rookSquare.children[0]);
                document.getElementById("f8").append(rook);
                return "k";
            }
            // Castling queen side. move the rook to the d file
            if (from == "e" && to == "c") {
                const rookSquare = document.getElementById("a8");
                const rook = rookSquare.removeChild(rookSquare.children[0]);
                document.getElementById("d8").append(rook);
                return "q";
            }
        }
    }

    /**
     * This is for the user move
     */
    doCastleUser() {
        const move = this.dataStore.get("move");
        const moveNotation = move.from.id + move.to.id;
        const piece = move.to.firstElementChild;
        const castled = this.doCastle(moveNotation, piece);
        move.castled = castled;
        this.dataStore.set("move", move);
    }

    /**
     * Submit the move to the database and draw the new move.
     */
    async submitMove() {
        const gameId = this.dataStore.get("gameId");
        const obj = this.dataStore.get("move");
        let move = null;
        if (obj.promoted) {
            move = obj.from.id + obj.to.id + obj.promoted;
        }
        else {
            move = obj.from.id + obj.to.id;
        }

        const submitButton = document.getElementById('submit');
        const cancelButton = document.getElementById('cancel');
        submitButton.disabled = true;
        cancelButton.disabled = true;
        const origButtonText = submitButton.innerText;
        submitButton.innerText = ' Loading...';
        const spinner = document.createElement("span");
        spinner.classList.add("spinner-border");
        spinner.classList.add("spinner-border-sm");
        spinner.setAttribute("role", "status");
        spinner.setAttribute("aria-hidden", "true");
        spinner.id = "submitSpinner";
        submitButton.prepend(spinner);

        const alerts = document.getElementById('errorAlerts');
        const response = await this.client.getNextMove(gameId, move, (error) => {
            submitButton.innerText = origButtonText;
            alerts.append(this.client.createAlert(`<strong>Error:</strong> ${error.message}`, "alert-danger"));
        });

        if (response) {
            document.getElementById('submit').innerText = origButtonText;
            document.getElementById('submit').disabled = true;
            document.getElementById('cancel').disabled = true;
            console.log(response);
            this.reloadMoves(response);
        }
        else {
            submitButton.disabled = false;
            cancelButton.disabled = false;
        }
        submitButton.removeChild(spinner);
    }

    /**
     * Reload the board after a move
     */
    reloadMoves(response) {
        // Do the response move
        let engineMove = response.move;
        if (engineMove) {
            let from = document.getElementById(engineMove.slice(0, 2));
            let to = document.getElementById(engineMove.slice(2, 4));
            if (to.firstElementChild) {
                to.removeChild(to.children[0]);
            }
            let promotion = engineMove.slice(4);
            let origPiece = from.firstElementChild;
            if (promotion && origPiece.classList.contains("white")) {
                if (promotion == "q") {
                    origPiece.innerHTML = "♕";
                }
                if (promotion == "b") {
                    origPiece.innerHTML = "♗";
                }
                if (promotion == "r") {
                    origPiece.innerHTML = "♖";
                }
                if (promotion == "n") {
                    origPiece.innerHTML = "♘";
                }
            }
            if (promotion && origPiece.classList.contains("black")) {
                if (promotion == "q") {
                    origPiece.innerHTML = "♛";document.getElementById(validFrom)
                }
                if (promotion == "b") {
                    origPiece.innerHTML = "♝";
                }
                if (promotion == "r") {
                    origPiece.innerHTML = "♜";
                }
                if (promotion == "n") {
                    origPiece.innerHTML = "♞";
                }
            }
            to.append(from.firstElementChild);
            this.doCastle(response.move, origPiece);
        }

        // Check for winner
        const winner = response.game.winner;
        if (winner) {
            const alerts = document.getElementById('errorAlerts');
            document.getElementById('resign').disabled = true;
            if (winner == "draw") {
                alerts.append(this.client.createAlert("The game has ended in a draw", "alert-info"));
            }
            else if (winner == "black") {
                alerts.append(this.client.createAlert("Black has won by checkmate", "alert-info"));
            }
            else {
                alerts.append(this.client.createAlert("White has won by checkmate", "alert-info"));
            }
        }

        // Remove all previous valid moves
        const collection = document.getElementsByTagName("chess-piece");
        for (var i = 0; i < collection.length; i++) {
            collection[i].validMoves = [];
        }
        
        // Make the new valid moves
        if (response.game.validMoves) {
            response.game.validMoves.split(",").filter(move => move != "").forEach((move) => {
                let validFrom = move.slice(0, 2);
                let validTo = move.slice(2, 4);
                const piece = document.getElementById(validFrom).firstElementChild;
                piece.validMoves.push(validTo);
                piece.addEventListener('mousedown', this.drag);
            });
        }
    }

    /**
     * Cancel a pending move
     */
    cancel() {
        const move = this.dataStore.get("move");
        console.log(move);
        const piece = move.to.removeChild(move.to.children[0]);
        if (move.captured) {
            move.to.append(move.captured);
        }
        if (move.promoted) {
            piece.innerHTML = "♙";
        }
        if (move.castled) {
            if (move.castled == "K") {
                document.getElementById("h1").append(document.getElementById("f1").removeChild(document.getElementById("f1").children[0]));
            }
            if (move.castled == "Q") {
                document.getElementById("a1").append(document.getElementById("d1").removeChild(document.getElementById("d1").children[0]));
            }
            if (move.castled == "k") {
                document.getElementById("h8").append(document.getElementById("f8").removeChild(document.getElementById("f8").children[0]));
            }
            if (move.castled == "q") {
                document.getElementById("a8").append(document.getElementById("d8").removeChild(document.getElementById("d8").children[0]));
            }
        }
        move.from.append(piece);
        document.getElementById('submit').disabled = true;
        document.getElementById('cancel').disabled = true;
    }

    async resign() {
        const confirm = window.confirm("Are you sure you want to resign?");

        const resignButton = document.getElementById('resign');
        const origButtonText = resignButton.innerText;
        resignButton.innerText = ' Loading...';
        const spinner = document.createElement("span");
        spinner.classList.add("spinner-border");
        spinner.classList.add("spinner-border-sm");
        spinner.setAttribute("role", "status");
        spinner.setAttribute("aria-hidden", "true");
        spinner.id = "resignSpinner";
        resignButton.prepend(spinner);

        if (confirm) {
            const alerts = document.getElementById('errorAlerts');
            const response = await this.client.resign(this.dataStore.get("gameId"), (error) => {
                submitButton.innerText = origButtonText;
                alerts.append(this.client.createAlert(`<strong>Error:</strong> ${error.message}`, "alert-danger"));
            });

            if (response) {
                alerts.append(this.client.createAlert("The game has ended in resignation", "alert-info"));
                document.getElementById('resign').disabled = true;
                const collection = document.getElementsByTagName("chess-piece");
                for (var i = 0; i < collection.length; i++) {
                    collection[i].validMoves = [];
                }
            }
        }
        resignButton.innerText = origButtonText;
        resignButton.removeChild(spinner);
    }
}


class ChessPiece extends HTMLElement {}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const game = new GetNextMove();
    game.mount();
};

window.addEventListener('DOMContentLoaded', main);