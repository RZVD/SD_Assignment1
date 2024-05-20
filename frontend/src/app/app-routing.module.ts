import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {UsersComponent} from './users/users.component';
import {QuestionsComponent} from './questions/questions.component';
import {UserComponent} from './user/user.component';

const routes: Routes = [
    { path: 'questions', component: QuestionsComponent },
    { path: 'users', component: UsersComponent },
    { path: "user/:id", component: UserComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
