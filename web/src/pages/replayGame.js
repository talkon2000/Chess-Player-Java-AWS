import ChessPlayerClient from '../api/chessPlayerClient';
import Header from '../components/header';
import BindingClass from '../utils/bindingClass';
import DataStore from '../utils/dataStore';

/**
 * The component that handles the logic to play chess for the website.
 */
export default class GetNextMove extends BindingClass {

    constructor() {
        super();
        this.bindClassMethods(['mount', 'doCastle', 'forward', 'back'], this);
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
        const errorMessageDisplay = document.getElementById('error-message');
        // Get game
        const response = await this.client.getGame(gameId, (error) => {
             errorMessageDisplay.innerText = `Error: ${error.message}`;
             errorMessageDisplay.classList.remove('hidden');
        });

        const game = response.game;

        if (game.active != "false") {
             errorMessageDisplay.innerText = `Error: Cannot replay a game that is still active`;
             errorMessageDisplay.classList.remove('hidden');
             return;
        }


        document.getElementById('forward').addEventListener('click', this.forward);
        document.getElementById('back').addEventListener('click', this.back);

        this.dataStore.set("moves", game.moves);
        this.dataStore.set("currentMove", -1);
    }

    /**
     * Method to handle castling. For use with engine moves
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
     * Render the next move.
     */
     async forward() {
         const errorMessageDisplay = document.getElementById('error-message');
         errorMessageDisplay.innerText = '';
         errorMessageDisplay.classList.add('hidden');

        const moves = this.dataStore.get("moves");
        const currentMove = this.dataStore.get("currentMove");

        let move = moves[currentMove + 1];
        const propertyName = "move" + (currentMove + 1);
        var captured = null, promoted = null, castled = null;

        if (move) {
            let from = document.getElementById(move.slice(0, 2));
            let to = document.getElementById(move.slice(2, 4));
            if (to.firstElementChild) {
                captured = to.removeChild(to.children[0]);
            }
            let promotion = move.slice(4);
            if (promotion) {
                promoted = true;
            }
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
            castled = this.doCastle(response.move, origPiece);
        }
        if (!this.dataStore.get(propertyName)) {
            this.dataStore.set(propertyName, {"captured": captured, "promoted": promoted, "castled": castled});
        }
        this.dataStore.set("currentMove", currentMove + 1);
     }

    /**
     * Go back one move
     */
     back() {
        const moves = this.dataStore.get("moves");
        const currentMove = this.dataStore.get("currentMove");
        const move = moves[currentMove];
        const from = document.getElementById(move.slice(0, 2));
        const to = document.getElementById(move.slice(2, 4));
        const piece = to.removeChild(move.to.children[0]);
        if (move.captured) {
            to.append(move.captured);
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
        this.dataStore.set("currentMove", currentMove - 1);
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