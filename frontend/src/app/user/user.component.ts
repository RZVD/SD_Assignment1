import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {MatCardModule} from '@angular/material/card';
import {NgFor, NgIf} from '@angular/common';

@Component({
    standalone: true,
    selector: 'app-user',
    imports: [MatCardModule, NgFor, NgIf],
    templateUrl: './user.component.html',
    styleUrl: './user.component.scss'
})
export class UserComponent implements OnInit{
    public userId: any;
    public user: any;


    mockUsers = [
        {
            id: 1,
            username: "SomeAwesomeUser",
            password: "totally_insecure",
            roles: ["REGULAR", "MODERATOR"]
        },
        {
            id: 2,
            username: "RZVD",
            password: "super_secure password",
            roles: ["REGULAR", "MODERATOR"]
        },
        {
            id: 3,
            username: "some_guy",
            password: "totally_insecure",
            roles: ["REGULAR", "MODERATOR"]
        },
        {
            username: "eyeberg",
            password: "totally_insecure",
            roles: ["REGULAR", "MODERATOR"]
        },
        {
            id: 4,
            username: "some_other_guy",
            password: "totally_insecure",
            roles: ["REGULAR", "MODERATOR"]
        },
        {
            id: 5,
            username: "nobody",
            password: "totally_insecure",
            roles: ["REGULAR", "MODERATOR"]
        },
    ]

    constructor(private route: ActivatedRoute) { }
    ngOnInit(): void {
        this.userId = this.route.snapshot.paramMap.get("id")!;
        this.user = this.mockUsers.find(user => user.id == +this.userId || user.username == this.userId);
    }

}
