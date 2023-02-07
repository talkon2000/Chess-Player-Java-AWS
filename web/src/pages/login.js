import ChessPlayerClient from '../api/chessPlayerClient';

export default class Login {

    constructor() {
        this.mount();
    }

    async mount() {
        this.client = new ChessPlayerClient();
        console.log(await this.client.getTokenOrThrow("None"));
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const login = new Login();
};

window.addEventListener('DOMContentLoaded', main);