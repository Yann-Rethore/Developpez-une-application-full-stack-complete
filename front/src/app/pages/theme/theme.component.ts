import { Component, OnInit, OnDestroy } from '@angular/core';
import { TopicService } from '../../services/topic.service';
import { TopicDto } from '../../interfaces/topic.dto';
import { Observable, BehaviorSubject, Subject, switchMap } from 'rxjs';

/**
 * Composant de gestion des thèmes.
 * Permet d'afficher la liste des thèmes et de s'abonner à un thème.
 */
@Component({
  selector: 'app-theme',
  templateUrl: './theme.component.html',
  styleUrls: ['./theme.component.scss']
})
export class ThemeComponent implements OnInit, OnDestroy {
  /** Subject pour rafraîchir la liste des thèmes et abonnements */
  private refresh$ = new BehaviorSubject<void>(undefined);

  /** Observable de la liste des thèmes */
  topics$!: Observable<TopicDto[]>;
  /** Observable des identifiants des thèmes auxquels l'utilisateur est abonné */
  subscribedTopicIds$!: Observable<number[]>;
  /** Subject pour gérer la destruction des subscriptions */
  private destroy$ = new Subject<void>();

  /**
   * Constructeur injectant le service de gestion des thèmes.
   * @param topicService Service pour récupérer et gérer les thèmes
   */
  constructor(private topicService: TopicService) {}

  /**
   * Initialise les observables pour charger les thèmes et les abonnements de l'utilisateur.
   */
  ngOnInit(): void {
    this.topics$ = this.refresh$.pipe(
      switchMap(() => this.topicService.getAllTopics())
    );
    this.subscribedTopicIds$ = this.refresh$.pipe(
      switchMap(() => this.topicService.getUserSubscriptions())
    );
  }

  /**
   * Vérifie si l'utilisateur est abonné à un thème donné.
   * @param topicId Identifiant du thème
   * @param subscribedIds Liste des identifiants des thèmes abonnés
   * @returns true si abonné, false sinon
   */
  isSubscribed(topicId: number, subscribedIds: number[]): boolean {
    return subscribedIds.includes(Number(topicId));
  }

  /**
   * Abonne l'utilisateur à un thème si ce n'est pas déjà fait, puis rafraîchit la liste.
   * @param topicId Identifiant du thème à abonner
   * @param subscribedIds Liste des identifiants des thèmes abonnés
   */
  toggleSubscription(topicId: number, subscribedIds: number[]) {
    if (this.isSubscribed(topicId, subscribedIds)) {
      // Ne rien faire, bouton désactivé
      return;
    }
    this.topicService.subscribeToTopic(topicId).subscribe(() => {
      this.refresh$.next();
    });
  }

  /**
   * Nettoie les subscriptions lors de la destruction du composant.
   */
  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}