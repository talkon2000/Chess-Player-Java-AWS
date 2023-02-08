import axios from "axios";
import BindingClass from "../utils/bindingClass";
import Authenticator from "./authenticator";

/**
 * Client to call the ChessPlayerService.
 *
 * This could be a great place to explore Mixins. Currently the client is being loaded multiple times on each page,
 * which we could avoid using inheritance or Mixins.
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Classes#Mix-ins
 * https://javascript.info/mixins
  */
export default class ChessPlayerClient extends BindingClass {

    constructor(props = {}) {
        super();

        const methodsToBind = ['clientLoaded', 'getIdentity', 'login', 'logout', 'getGame', 'getNextMove', 'searchUsers'];
        this.bindClassMethods(methodsToBind, this);

        this.authenticator = new Authenticator();;
        this.props = props;

        axios.defaults.baseURL = process.env.API_BASE_URL;
        this.axiosClient = axios;
        this.clientLoaded();
    }

    /**
     * Run any functions that are supposed to be called once the client has loaded successfully.
     */
    clientLoaded() {
        if (this.props.hasOwnProperty("onReady")) {
            this.props.onReady(this);
        }
    }

    /**
     * Get the identity of the current user
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The user information for the current user.
     */
    async getIdentity(errorCallback) {
        try {
            const isLoggedIn = await this.authenticator.isUserLoggedIn();

            if (!isLoggedIn) {
                return undefined;
            }

            return await this.authenticator.getCurrentUserInfo();
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async login() {
        this.authenticator.login();
    }

    async logout() {
        this.authenticator.logout();
    }

    async getTokenOrThrow(unauthenticatedErrorMessage) {
        const isLoggedIn = await this.authenticator.isUserLoggedIn();
        if (!isLoggedIn) {
            throw new Error(unauthenticatedErrorMessage);
        }

        return await this.authenticator.getUserToken();
    }

    /**
     * Creates a new game in the database, and associates it with the user(s) database entries
     * @param authUserWhite boolean indicating if the user creating the game is white or black
     * @param otherUserId OR botDifficulty
     * @param errorCallback (optional) function to perform if an error occurs
     * @returns new game's ID
     */
     async createGame(authUserWhite, otherUserId, botDifficulty, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can create a new game.");
            let payload = {"authUserWhite": authUserWhite};
            if (otherUserId) {
                payload.otherUserId = otherUserId;
            }
            if (botDifficulty) {
                payload.botDifficulty = botDifficulty;
            }
            const response = await this.axiosClient.post(`game/`, payload, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            return response.data;
        }
        catch (error) {
            errorCallback(error);
        }
     }

    /**
     * Retrieves a game from the database
     */
    async getGame(gameId, errorCallback) {
        try {
            const response = await this.axiosClient.get(`game/${gameId}`);
            return response.data;
        } catch (error) {
            this.handleError(error, errorCallback);
        }
    }

    /**
     * Gets the next move for the given game ID.
     * @param gameId Unique identifier for a game
     * @param move The move the user wants to make
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The engine move, a game object including valid moves, and metadata.
     */
    async getNextMove(gameId, move, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can make a move.");
            const response = await this.axiosClient.get(`move/${move}?gameId=${gameId}`,{
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            return response.data;
        } catch (error) {
            this.handleError(error, errorCallback);
        }
    }

    /**
     * Gets a user based on the auth token
     * @param errorCallback the function to execute if the call fails.
     * @returns A user object if the user exists
     */
     async getPrivateUser(errorCallback) {
        try {
            const token = await this.getTokenOrThrow("You are not logged in!");
            const response = await this.axiosClient.get(`users/`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            return response.data;
        }
        catch (error) {
            errorCallback(error);
        }
     }

    /**
     * Gets a user's public profile based on user ID
     * @param errorCallback the function to execute if the call fails.
     * @returns A user object if the user exists
     */
     async getPublicUser(username, errorCallback) {
        try {
            const response = await this.axiosClient.get(`users/public/${username}`);
            return response.data;
        }
        catch (error) {
            errorCallback(error);
        }
     }

    /**
     * Creates a user in the Database
     * @param errorCallback the function to execute if the call fails.
     */
     async createUser(errorCallback) {
        try {
            const token = await this.getTokenOrThrow("You are not logged in!");
            const response = await this.axiosClient.post(`users/`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
        }
        catch (error) {
            errorCallback(error);
        }
     }

    /**
     * Search for a user.
     * @param criteria A string containing search criteria to pass to the API.
     * @returns The user that matches the search criteria.
     */
    async searchUsers(criteria, errorCallback) {
        try {
            const response = await this.axiosClient.get(`users/${criteria}`);

            return response.data;
        } catch (error) {
            this.handleError(error, errorCallback);
        }
    }

    /**
     * Helper method to log the error and run any error functions.
     * @param error The error received from the server.
     * @param errorCallback (Optional) A function to execute if the call fails.
     */
    handleError(error, errorCallback) {
        console.error(error);

        const errorFromApi = error?.response?.data?.error_message;
        if (errorFromApi) {
            console.error(errorFromApi)
            error.message = errorFromApi;
        }

        if (errorCallback) {
            errorCallback(error);
        }
    }
}