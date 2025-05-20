import { Routes } from '@angular/router';
import { PdfPageComponent } from './components/pdf/pdf-page/pdf-page.component';

export const routes: Routes = [
    {
        path: '',
        redirectTo: 'pdf',
        pathMatch: 'full',},
    {
        path: 'pdf',
        component: PdfPageComponent,
    },
];
