import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {UsersComponent} from './views/users/users.component';
import {QuestionsComponent} from './views/questions/questions.component';
import {UserComponent} from './views/user/user.component';
import { QuestionComponent } from './question/question.component';
import { LoginComponent } from './login/login.component';

const routes: Routes = [
    { path: 'questions', component: QuestionsComponent },
    { path: 'users', component: UsersComponent },
    { path: "user/:id", component: UserComponent},
    { path: "question/:id", component: QuestionComponent},
    { path: "login", component: LoginComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
