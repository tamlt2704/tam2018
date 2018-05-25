import os
import sys
import shutil 

from django.conf import settings
from django.core.management import call_command 
from django.core.management.base import BaseCommand, CommandError 
from django.core.urlresolvers import reverse 
from django.test.client import Client

def get_pages():
	for name in os.listdir(settings.SITE_PAGES_DIRECTORY):
		if name.endswith('.html'):
			yield name[:-5]

class Command(BaseCommand):
	help ='build static site output'

	def add_arguments(self, parser):
		parser.add_argument('args', nargs='*')

	def handle(*args, **options):
		if args:
			pages = args[1:]
			available = list(get_pages())
			invalid = []

			for page in pages:
				if page not in available:
					invalid.append(page)

			if invalid:
				msg = 'Invalid Pages {}'.format(','.join(invalid))
				raise CommandError(msg) 	

		else:
			pages = get_pages()
		
			if os.path.exists(settings.SITE_OUTPUT_DIRECTORY):
				shutil.rmtree(settings.SITE_OUTPUT_DIRECTORY)
			os.mkdir(settings.SITE_OUTPUT_DIRECTORY)
			os.mkdir(settings.STATIC_ROOT)

		call_command('collectstatic', interactive=False, clear=True, verbosity=0)
		client = Client()
		for page in get_pages():
			url = reverse('page', kwargs={'slug': page})
			response = client.get(url)

			if page == 'index':
				output_dir = settings.SITE_OUTPUT_DIRECTORY
			else:
				output_dir = os.path.join(settings.SITE_OUTPUT_DIRECTORY, page)
				if not os.path.exists(output_dir):
					os.mkdir(output_dir)

			with open(os.path.join(output_dir, 'index.html'), 'wb') as f:
				f.write(response.content)
