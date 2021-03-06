require 'spec_helper'

describe "rack lib/-based jar loading" do

  deploy :path => "rack/lib-jar-knob.yml"

  it "should work" do
    visit "/lib-jar"
    puts "==="
    puts page.body.to_s
    puts "==="
    uuid = page.find( :xpath, '//pre' ).text
    uuid.should match(/^........-....-....-....-............$/)
  end

end
