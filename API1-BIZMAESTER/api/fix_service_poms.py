from pathlib import Path
modules = [
    'service-commerce',
    'service-construction',
    'service-ferme',
    'service-education',
    'service-hotellerie',
    'service-logistique',
    'service-restauration',
    'service-sante',
    'service-transport',
]
for m in modules:
    p = Path(m) / 'pom.xml'
    import re
    text = p.read_text(encoding='utf-8')
    if '<artifactId>service-template</artifactId>' in text:
        pattern = re.compile(r'(<dependency>\s*<groupId>com\.bizmaster</groupId>\s*<artifactId>service-template</artifactId>)(?:\s*<version>.*?</version>)*\s*</dependency>', re.DOTALL)
        new_text = pattern.sub(r'\1\n                <version>${project.version}</version>\n        </dependency>', text)
        if new_text != text:
            p.write_text(new_text, encoding='utf-8')
            print('fixed', p)
