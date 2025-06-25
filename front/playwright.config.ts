import * as path from 'path';

const config = {
  testDir: './tests', // <-- Ajoute cette ligne pour cibler uniquement le dossier E2E
  reporter: [
    ['list'],
    [
      '@bgotink/playwright-coverage',
      {
        sourceRoot: path.join(__dirname, 'src'),
        resultDir: path.join(__dirname, 'coverage/e2e'),
        reports: [
          ['html'],
          ['lcovonly', { file: 'coverage.lcov' }],
          ['text-summary'],
        ],
      },
    ],
  ],
  // Ajoute ici d'autres options Playwright si besoin
};

export default config;
