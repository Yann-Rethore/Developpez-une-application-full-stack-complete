import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ArticleService } from './article.service';
import { environment } from '../../environments/environment';
import { ArticleCreateDTO } from '../interfaces/article-create.dto';
import { ArticleDto } from '../interfaces/article.dto';

describe('ArticleService', () => {
  let service: ArticleService;
  let httpMock: HttpTestingController;
  const apiUrl = `${environment.apiUrl}/article`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ArticleService]
    });
    service = TestBed.inject(ArticleService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call POST on createArticle', () => {
    const dto: ArticleCreateDTO = { titre: 'Titre', contenu: 'Contenu', themeId: 1 };
    service.createArticle(dto).subscribe();
    const req = httpMock.expectOne(apiUrl);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(dto);
    req.flush({});
  });

  it('should call GET on getArticlesAbonnes', () => {
    const mockArticles: ArticleDto[] = [];
    service.getArticlesAbonnes().subscribe(articles => {
      expect(articles).toEqual(mockArticles);
    });
    const req = httpMock.expectOne(`${apiUrl}/abonnes`);
    expect(req.request.method).toBe('GET');
    req.flush(mockArticles);
  });

  it('should call GET on getArticleById', () => {
    const mockArticle: ArticleDto = {
      id: 1,
      titre: 'Titre',
      date: '2024-01-01',
      contenu: 'Contenu',
      themeName: 'Thème',
      auteurUsername: 'Auteur',
      commentaires: []
    };
    service.getArticleById(1).subscribe(article => {
      expect(article).toEqual(mockArticle);
    });
    const req = httpMock.expectOne(`${apiUrl}/1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockArticle);
  });

  it('should call POST on ajouterCommentaire', () => {
    service.ajouterCommentaire(1, 'Mon commentaire').subscribe();
    const req = httpMock.expectOne(`${apiUrl}/1/commentaire`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ contenu: 'Mon commentaire' });
    req.flush({});
  });
});