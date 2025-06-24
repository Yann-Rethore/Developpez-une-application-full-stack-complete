const fs = require('fs');
const path = require('path');
const v8toIstanbul = require('v8-to-istanbul');
const istanbulCoverage = require('istanbul-lib-coverage');
const istanbulReport = require('istanbul-lib-report');
const istanbulReports = require('istanbul-reports');


(async () => {
  const raw = JSON.parse(fs.readFileSync('./coverage/e2ecoverage.json', 'utf-8'));
  const map = istanbulCoverage.createCoverageMap({});

  for (const entry of raw) {
    try {
      const converter = v8toIstanbul(entry.url, 0, { source: entry.source });
      await converter.load();
      converter.applyCoverage(entry.functions);
      const fileCoverage = converter.toIstanbul();
      map.merge(fileCoverage);
    } catch (err) {
      console.warn(`⚠️ Fichier ignoré : ${entry.url}\n  → Raison : ${err.message}`);
    }
  }

  const context = istanbulReport.createContext({
    dir: './coverage/e2e-html',
    coverageMap: map,
  });

  const report = istanbulReports.create('html');
  report.execute(context);

  console.log('\n✅ Rapport HTML généré dans ./coverage/e2e-html/index.html');
})();