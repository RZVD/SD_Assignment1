import {Component} from '@angular/core';
import {MatCardModule} from '@angular/material/card'
import {NgFor} from '@angular/common';

@Component({
    standalone: true,
    selector: 'app-users',
    templateUrl: './users.component.html',
    imports: [MatCardModule, NgFor],
    styleUrl: './users.component.scss'
})
export class UsersComponent {
    mockUsers = [
        {
            id: 1,
            username: "SomeAwesomeUser",
            password: "totally_insecure"
        },
        {
            id: 2,
            username: "RZVD",
            password: "super_secure password"
        },
        {
            id: 3,
            username: "some_guy",
            password: "totally_insecure"
        },
        {
            username: "a",
            password: "totally_insecure"
        },
        {
            id: 4,
            username: "some_other_guy",
            password: "totally_insecure"
        },
        {
            id: 5,
            username: "nobody",
            password: "totally_insecure"
        },
]
}
