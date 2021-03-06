# -*- encoding: utf-8 -*-

Gem::Specification.new do |s|
  s.name = %q{sinatra}
  s.version = "1.1.0"

  s.required_rubygems_version = Gem::Requirement.new(">= 0") if s.respond_to? :required_rubygems_version=
  s.authors = ["Blake Mizerany", "Ryan Tomayko", "Simon Rozet", "Konstantin Haase"]
  s.date = %q{2010-10-23}
  s.description = %q{Classy web-development dressed in a DSL}
  s.email = %q{sinatrarb@googlegroups.com}
  s.extra_rdoc_files = ["README.rdoc", "README.de.rdoc", "README.jp.rdoc", "README.fr.rdoc", "README.es.rdoc", "README.hu.rdoc", "README.zh.rdoc", "LICENSE"]
  s.files = ["AUTHORS", "CHANGES", "LICENSE", "README.de.rdoc", "README.es.rdoc", "README.fr.rdoc", "README.hu.rdoc", "README.jp.rdoc", "README.rdoc", "README.zh.rdoc", "Rakefile", "lib/sinatra.rb", "lib/sinatra/base.rb", "lib/sinatra/images/404.png", "lib/sinatra/images/500.png", "lib/sinatra/main.rb", "lib/sinatra/showexceptions.rb", "sinatra.gemspec", "test/base_test.rb", "test/builder_test.rb", "test/coffee_test.rb", "test/contest.rb", "test/encoding_test.rb", "test/erb_test.rb", "test/erubis_test.rb", "test/extensions_test.rb", "test/filter_test.rb", "test/haml_test.rb", "test/hello.mab", "test/helper.rb", "test/helpers_test.rb", "test/less_test.rb", "test/liquid_test.rb", "test/mapped_error_test.rb", "test/markaby_test.rb", "test/markdown_test.rb", "test/middleware_test.rb", "test/nokogiri_test.rb", "test/public/favicon.ico", "test/radius_test.rb", "test/rdoc_test.rb", "test/request_test.rb", "test/response_test.rb", "test/result_test.rb", "test/route_added_hook_test.rb", "test/routing_test.rb", "test/sass_test.rb", "test/scss_test.rb", "test/server_test.rb", "test/settings_test.rb", "test/sinatra_test.rb", "test/static_test.rb", "test/templates_test.rb", "test/textile_test.rb", "test/views/ascii.haml", "test/views/error.builder", "test/views/error.erb", "test/views/error.erubis", "test/views/error.haml", "test/views/error.sass", "test/views/explicitly_nested.str", "test/views/foo/hello.test", "test/views/hello.builder", "test/views/hello.coffee", "test/views/hello.erb", "test/views/hello.erubis", "test/views/hello.haml", "test/views/hello.less", "test/views/hello.liquid", "test/views/hello.mab", "test/views/hello.md", "test/views/hello.nokogiri", "test/views/hello.radius", "test/views/hello.rdoc", "test/views/hello.sass", "test/views/hello.scss", "test/views/hello.str", "test/views/hello.test", "test/views/hello.textile", "test/views/layout2.builder", "test/views/layout2.erb", "test/views/layout2.erubis", "test/views/layout2.haml", "test/views/layout2.liquid", "test/views/layout2.mab", "test/views/layout2.nokogiri", "test/views/layout2.radius", "test/views/layout2.str", "test/views/layout2.test", "test/views/nested.str", "test/views/utf8.haml"]
  s.homepage = %q{http://sinatra.rubyforge.org}
  s.rdoc_options = ["--line-numbers", "--inline-source", "--title", "Sinatra", "--main", "README.rdoc"]
  s.require_paths = ["lib"]
  s.rubyforge_project = %q{sinatra}
  s.rubygems_version = %q{1.3.6}
  s.summary = %q{Classy web-development dressed in a DSL}
  s.test_files = ["test/base_test.rb", "test/builder_test.rb", "test/coffee_test.rb", "test/encoding_test.rb", "test/erb_test.rb", "test/erubis_test.rb", "test/extensions_test.rb", "test/filter_test.rb", "test/haml_test.rb", "test/helpers_test.rb", "test/less_test.rb", "test/liquid_test.rb", "test/mapped_error_test.rb", "test/markaby_test.rb", "test/markdown_test.rb", "test/middleware_test.rb", "test/nokogiri_test.rb", "test/radius_test.rb", "test/rdoc_test.rb", "test/request_test.rb", "test/response_test.rb", "test/result_test.rb", "test/route_added_hook_test.rb", "test/routing_test.rb", "test/sass_test.rb", "test/scss_test.rb", "test/server_test.rb", "test/settings_test.rb", "test/sinatra_test.rb", "test/static_test.rb", "test/templates_test.rb", "test/textile_test.rb"]

  if s.respond_to? :specification_version then
    current_version = Gem::Specification::CURRENT_SPECIFICATION_VERSION
    s.specification_version = 2

    if Gem::Version.new(Gem::RubyGemsVersion) >= Gem::Version.new('1.2.0') then
      s.add_runtime_dependency(%q<rack>, ["~> 1.1"])
      s.add_runtime_dependency(%q<tilt>, ["~> 1.1"])
      s.add_development_dependency(%q<rake>, [">= 0"])
      s.add_development_dependency(%q<shotgun>, ["~> 0.6"])
      s.add_development_dependency(%q<rack-test>, [">= 0.5.6"])
      s.add_development_dependency(%q<haml>, [">= 3.0"])
      s.add_development_dependency(%q<builder>, [">= 0"])
      s.add_development_dependency(%q<erubis>, [">= 0"])
      s.add_development_dependency(%q<less>, [">= 0"])
      s.add_development_dependency(%q<liquid>, [">= 0"])
      s.add_development_dependency(%q<rdiscount>, [">= 0"])
      s.add_development_dependency(%q<RedCloth>, [">= 0"])
      s.add_development_dependency(%q<radius>, [">= 0"])
      s.add_development_dependency(%q<markaby>, [">= 0"])
      s.add_development_dependency(%q<coffee-script>, [">= 0"])
      s.add_development_dependency(%q<rdoc>, [">= 0"])
      s.add_development_dependency(%q<nokogiri>, [">= 0"])
    else
      s.add_dependency(%q<rack>, ["~> 1.1"])
      s.add_dependency(%q<tilt>, ["~> 1.1"])
      s.add_dependency(%q<rake>, [">= 0"])
      s.add_dependency(%q<shotgun>, ["~> 0.6"])
      s.add_dependency(%q<rack-test>, [">= 0.5.6"])
      s.add_dependency(%q<haml>, [">= 3.0"])
      s.add_dependency(%q<builder>, [">= 0"])
      s.add_dependency(%q<erubis>, [">= 0"])
      s.add_dependency(%q<less>, [">= 0"])
      s.add_dependency(%q<liquid>, [">= 0"])
      s.add_dependency(%q<rdiscount>, [">= 0"])
      s.add_dependency(%q<RedCloth>, [">= 0"])
      s.add_dependency(%q<radius>, [">= 0"])
      s.add_dependency(%q<markaby>, [">= 0"])
      s.add_dependency(%q<coffee-script>, [">= 0"])
      s.add_dependency(%q<rdoc>, [">= 0"])
      s.add_dependency(%q<nokogiri>, [">= 0"])
    end
  else
    s.add_dependency(%q<rack>, ["~> 1.1"])
    s.add_dependency(%q<tilt>, ["~> 1.1"])
    s.add_dependency(%q<rake>, [">= 0"])
    s.add_dependency(%q<shotgun>, ["~> 0.6"])
    s.add_dependency(%q<rack-test>, [">= 0.5.6"])
    s.add_dependency(%q<haml>, [">= 3.0"])
    s.add_dependency(%q<builder>, [">= 0"])
    s.add_dependency(%q<erubis>, [">= 0"])
    s.add_dependency(%q<less>, [">= 0"])
    s.add_dependency(%q<liquid>, [">= 0"])
    s.add_dependency(%q<rdiscount>, [">= 0"])
    s.add_dependency(%q<RedCloth>, [">= 0"])
    s.add_dependency(%q<radius>, [">= 0"])
    s.add_dependency(%q<markaby>, [">= 0"])
    s.add_dependency(%q<coffee-script>, [">= 0"])
    s.add_dependency(%q<rdoc>, [">= 0"])
    s.add_dependency(%q<nokogiri>, [">= 0"])
  end
end
