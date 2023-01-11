import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SearchComponent } from './search/search.component';
import { BookingsComponent } from './bookings/bookings.component';
//import { BrowserModule } from '@anglar/platform-browser';
//import { ReactiveFormsModule } from '@angular/forms';

const routes: Routes = [
  { path: 'search-component', component: SearchComponent },
  { path: 'bookings-component', component: BookingsComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule { }
