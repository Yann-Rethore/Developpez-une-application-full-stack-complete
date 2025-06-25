import { Component, OnInit } from '@angular/core';

/**
 * Composant d'en-tête public de l'application.
 * Affiche la barre de navigation pour les utilisateurs non connectés.
 */
@Component({
  selector: 'app-header-public',
  templateUrl: './header-public.component.html',
  styleUrls: ['./header-public.component.scss']
})
export class HeaderPublicComponent implements OnInit {

  /**
   * Constructeur du composant d'en-tête public.
   * Aucun service n'est injecté car ce composant est purement visuel.
   */
  constructor() { }

  /**
   * Méthode du cycle de vie appelée à l'initialisation du composant.
   * Peut être utilisée pour une logique d'initialisation si besoin.
   */
  ngOnInit(): void {
  }

}