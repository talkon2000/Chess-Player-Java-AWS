import ChessPlayerClient from '../api/chessPlayerClient';
import BindingClass from "../utils/bindingClass";

/**
 * The header component for the website.
 */
export default class Header extends BindingClass {
    constructor() {
        super();

        const methodsToBind = [
            'addHeaderToPage', 'createUserInfoForHeader',
            'createLoginButton', 'createLogoutButton', 'createButton'
        ];
        this.bindClassMethods(methodsToBind, this);

        this.client = new ChessPlayerClient();
    }

    /**
     * Add the header to the page.
     */
    async addHeaderToPage() {
        const currentUser = await this.client.getIdentity();
        const userInfo = this.createUserInfoForHeader(currentUser);
        if (currentUser) {
            document.getElementById("myAccount").classList.remove("hidden");
        }

        const navItem = document.createElement('li');
        navItem.classList.add("nav-item");

        document.getElementById("currentUser").appendChild(navItem);
        navItem.appendChild(userInfo);
    }

    createUserInfoForHeader(currentUser) {
        const userInfo = document.createElement('li');
        userInfo.classList.add('user');

        const childContent = currentUser
            ? this.createLogoutButton(currentUser)
            : this.createLoginButton();

        userInfo.appendChild(childContent);

        return userInfo;
    }

    createLoginButton() {
        const button = this.createButton('Login', this.client.login);
        button.classList.add("btn-success");
        return button;
    }

    createLogoutButton(currentUser) {
        const button = this.createButton(`Logout: ${currentUser.username}`, this.client.logout);
        button.classList.add("btn-info");
        return button;
    }

    createButton(text, clickHandler) {
        const button = document.createElement('a');
        button.classList.add("btn");
        button.href = '#';
        button.innerText = text;

        button.addEventListener('click', async () => {
            await clickHandler();
        });

        return button;
    }
}