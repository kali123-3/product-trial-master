import {Component, input, OnInit, ViewEncapsulation} from '@angular/core';
import {NgIf} from "@angular/common";
import { Product } from "app/products/data-access/product.model";

@Component({
  selector: 'app-product-infos',
  standalone: true,
  imports: [
    NgIf
  ],
  templateUrl: './product-infos.component.html',
  styleUrl: './product-infos.component.css',
  encapsulation: ViewEncapsulation.None

})
export class ProductInfosComponent implements OnInit{
    public readonly selectedProduct = input.required<Product>();
    public imageUrl = input.required();

    constructor() {
    }

    ngOnInit(): void {

    }


}
